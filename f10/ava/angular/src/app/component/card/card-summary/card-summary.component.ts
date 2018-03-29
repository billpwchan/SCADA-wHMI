import { Component, OnInit, Input, Output, EventEmitter, SimpleChanges } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { SettingsService } from '../../../service/settings.service';
import { GetChildrenAliasesService } from '../../../service/scs/ava/dbm/get-children-aliases.service';
import { Subscription } from 'rxjs/Subscription';
import { AppSettings } from '../../../app-settings';
import { DbmSettings } from '../../../service/scadagen/dbm/dbm-settings';
import { DbmPollingService } from '../../../service/scs/dbm-polling.service';
import { OnDestroy, OnChanges } from '@angular/core/src/metadata/lifecycle_hooks';
import { DbmMultiReadAttrService } from '../../../service/scadagen/dbm/dbm-multi-read-attr.service';
import { Card } from '../../../model/Scenario';
import { CardsSettings } from '../cards/cards-settings';
import { AlarmSummarySettings, CardSummaryConfig } from '../../alarm/alarm-summary/alarm-summary-settings';
import { HttpAccessResult, HttpAccessResultType } from '../../../service/scadagen/access/http/Access-interface';

@Component({
  selector: 'app-card-summary',
  templateUrl: './card-summary.component.html',
  styleUrls: ['./card-summary.component.css']
})
export class CardSummaryComponent implements OnInit, OnDestroy, OnChanges {

  public static readonly STR_INIT = AppSettings.STR_INIT;

  readonly c: string = 'CardSummaryComponent';

  @Input() notifyFromParent: string;

  @Output() notifyParent: EventEmitter<string> = new EventEmitter();

  notifyCards: string;

  private cfg: CardSummaryConfig;
  @Input()
  set config(cfg: CardSummaryConfig) {
    const f = 'config';
    console.log(this.c, f);
    this.cfg = cfg;
  }

  private env: string;
  private index: number;
  private data: Map<number, Map<number, number>>;

  @Input()
  set updateCardEnv(env: string) {
    const f = 'updateCardEnv';
    console.log(this.c, f);
    this.env = env;
    console.log(this.c, f, 'this.env', this.env);
  }

  @Input()
  set renewCardSummary(date: Date) {
    const f = 'renewCardSummary';
    console.log(this.c, f);

    if ( null != date ) {
      this.reloadInstance();
    }
  }

  @Input()
  set updateCardSummary(date: Date) {
    const f = 'updateCardSummary';
    console.log(this.c, f);

    if ( null != date ) {
      this.reloadCard();
    }
  }

  @Output() onUpdatedCards: EventEmitter<Card[]> = new EventEmitter();
  @Output() onUpdatedCardsSelect: EventEmitter<number[]> = new EventEmitter();
  @Output() onUpdatedAvarAlias: EventEmitter<string> = new EventEmitter();
  @Output() onUpdatedAvasAliasList: EventEmitter<string[]> = new EventEmitter();

  getInstancesByClassNameSubscription: Subscription;
  getChildrenAliasSubscription: Subscription;
  multiReadSubscription: Subscription;
  dbmPollingSubscription: Subscription;

  updateCards: Card[];

  private className: string;
  private instances: string[];
  private avarAlias: string;
  private avasAliasList: string[];
  private ruleList: string[];
  private instanceRoot: string;

  private cards: Card[];
  private cardIdsSelected: number[];

  private subscriptsionStarted = false;

  constructor(
    private translate: TranslateService
    , private settingsService: SettingsService
    , private getChildrenAliasService: GetChildrenAliasesService
    , private dbmMultiReadAttrService: DbmMultiReadAttrService
    , private dbmPollingService: DbmPollingService
  ) { }

  ngOnInit() {
    const f = 'constructor';
    console.log(this.c, f);

    this.getChildrenAliasSubscription = this.getChildrenAliasService.dbmItem
    .subscribe(result => {
      console.log(this.c, f, 'getChildrenAliasSubscription', result);
      if ( null != result ) {
        if ( null != this.instanceRoot ) {
          const root: string = DbmSettings.STR_URL_ALIAS + this.instanceRoot;
          if ( root === result.dbAddress ) {

            const avaList: Array<string> = new Array<string>();
            for ( let i = 0 ; i < result.data.length ; ++i ) {
              const alias = result.data[i];
              avaList.push(alias + DbmSettings.STR_ATTR_SCSTYPE);
            }
            this.dbmMultiReadAttrService.read(this.env, avaList, CardsSettings.STR_READ_SCSTYPE);
          }
        }
        if ( null != this.avarAlias ) {
          if ( this.avarAlias === result.dbAddress ) {
            if ( null == this.ruleList ) {
              this.ruleList = new Array<string>();
            }
            for ( let i = 0 ; i < result.data.length; ++i ) {
              const alias = result.data[i];
              const classNames = alias.split(DbmSettings.STR_COLON);
              const className = classNames [classNames.length - 1];
              if ( className.startsWith(DbmSettings.STR_RULE) ) {

                let found = false;
                const base = this.cfg.ruleBase;
                const max = this.cfg.maxRuleNum;
                for ( let n = 0; n < max ; ++n ) {
                  const name = DbmSettings.STR_RULE + (DbmSettings.STR_THREE_ZERO + (n + base)).slice(-4);
                  if ( className === name ) {
                    found = true;
                  }
                }
                if ( found ) {
                  this.ruleList.push(alias);
                }
              }
            }

            // Sorting
            // this.ruleList.sort();
            this.ruleList.sort((a, b) => {
                const A = a.toUpperCase(); // ignore upper and lowercase
                const B = b.toUpperCase(); // ignore upper and lowercase
                if (A < B) {
                  return -1;
                }
                if (A > B) {
                  return 1;
                }
                // equal
                return 0;
            });

            this.reloadCard();
          }
        }
      }
    });

    this.multiReadSubscription = this.dbmMultiReadAttrService.dbmItem
    .subscribe( (res: HttpAccessResult) => {
      console.log(this.c, f, 'multiReadSubscription', res);
      if ( null != res ) {
        if ( HttpAccessResultType.NEXT === res.method ) {
          if ( CardsSettings.STR_READ_SCSTYPE === res.key ) {
            if ( null != res.values ) {
              this.avasAliasList = new Array<string>();

              for ( let i = 0 ; i < res.address.length ; ++i ) {
                const address: string = res.address[i];
                const alias = address.slice(0, address.length - (DbmSettings.STR_ATTR_SCSTYPE.length));
                const scstype = res.values[i];
                if ( scstype.startsWith(this.cfg.avarScstype) ) {
                  this.avarAlias = alias;
                  this.onUpdatedAvarAlias.emit(this.avarAlias);
                } else if ( scstype.startsWith(this.cfg.avasScstype) ) {
                  this.avasAliasList.push(alias);
                }
              }

              this.onUpdatedAvasAliasList.emit(this.avasAliasList);

              if ( null != this.avarAlias ) {
                this.getChildrenAliasService.readData(this.env, this.avarAlias);
              }
            }
          } else if ( CardsSettings.STR_READ_CARD === res.key ) {
            if ( null != res.values ) {
              this.renewCard(res.values);
              if ( ! this.subscriptsionStarted ) {
                this.subscriptsionStarted = true;
                const dbaddress: string[] = this.prepareReloadCard();
                this.dbmPollingService.subscribe(this.env, dbaddress);
              }
            }
          }
        }
      }
    });

    this.dbmPollingSubscription = this.dbmPollingService.dbmPollingItem
    .subscribe(result => {
      console.log(this.c, f, 'dbmPollingSubscription', result);
      if ( null != result ) {
        this.reloadCard();
      }
    });
  }

  ngOnDestroy(): void {
    const f = 'ngOnDestroy';
    console.log(this.c, f);
    // prevent memory leak when component is destroyed
    this.getInstancesByClassNameSubscription.unsubscribe();
    this.getChildrenAliasSubscription.unsubscribe();
    this.dbmPollingSubscription.unsubscribe();
  }

  ngOnChanges(changes: SimpleChanges): void {
    const f = 'ngOnChanges';
    console.log(this.c, f);
  }

  getNotification(evt) {
    const f = 'getNotification';
    // Do something with the notification (evt) sent by the child!
    console.log(this.c, f, 'evt', evt);
  }

  sendNotifyParent(str: string): void {
    const f = 'sendNotifyParent';
    console.log(this.c, f, str);
    this.notifyParent.emit(str);
  }

  private reloadInstance() {
    const f = 'reloadInstance';
    console.log(this.c, f);
    this.instanceRoot = this.cfg.instanceRoot;
    this.getChildrenAliasService.readData(this.env, DbmSettings.STR_URL_ALIAS + this.instanceRoot);
  }

  private prepareReloadCard(): string[] {
    const f = 'prepareReloadCard';
    console.log(this.c, f);
    const ret: string[] = new Array<string>();
    for ( let i = 0 ; i < this.ruleList.length ; ++i ) {
      for ( let j = 0 ; j < AlarmSummarySettings.RULE_ATTR_LIST.length ; ++j ) {
        const dbAttribute = this.ruleList[i] + AlarmSummarySettings.RULE_ATTR_LIST[j];
        ret.push(dbAttribute);
      }
    }
    return ret;
  }

  private renewCard(dbValue: any) {
    const f = 'renewCard';
    console.log(this.c, f);

    this.cards = [];

    for ( let i = 0 ; i < this.ruleList.length ; ++i ) {
      const base: number = i * AlarmSummarySettings.RULE_ATTR_LIST.length;
      let index = 0;
      const card: Card = new Card(
                                  dbValue[base + index++]
                                  , dbValue[base + index++]
                                  , dbValue[base + index++]
                                  , dbValue[base + index++]
                                  , dbValue[base + index++]
      );
      this.cards.push(card);
    }
    this.updateCards = this.cards;
    this.onUpdatedCards.emit(this.cards);
    this.onUpdatedCardsSelect.emit(this.cardIdsSelected);
  }

  private reloadCard() {
    const f = 'reloadCard';
    console.log(this.c, f);
    const dbAddresses: string[] = this.prepareReloadCard();
    this.dbmMultiReadAttrService.read(this.env, dbAddresses, CardsSettings.STR_READ_CARD);
  }

  private getCard(cardId: number): Card {
    const f = 'getSelectedCard';
    console.log(this.c, f);
    let cardSelected: Card = null;
    for ( let i = 0 ; i < this.cards.length ; ++i ) {
      const card: Card = this.cards[i];
      if ( cardId === card.index ) {
        cardSelected = card;
        break;
      }
    }
    return cardSelected;
  }

  private getCardSelected(): Card {
    let selectedCard: Card = null;
    if ( null != this.cardIdsSelected ) {
      if ( this.cardIdsSelected.length > 0 ) {
        selectedCard = this.getCard(this.cardIdsSelected[0]);
      }
    }
    return selectedCard;
  }

  onUpdatedCardSelection(cardIds: number[]) {
    const f = 'onUpdatedCardSelection';
    console.log(this.c, f);

    this.cardIdsSelected = cardIds;
    this.onUpdatedCardsSelect.emit(cardIds);

  }
}
