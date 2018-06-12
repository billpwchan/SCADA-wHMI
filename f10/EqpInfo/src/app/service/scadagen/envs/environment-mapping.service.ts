import { Injectable } from '@angular/core';

@Injectable()
export class EnvironmentMappingService {

  readonly c = 'EnvironmentMappingService';

  envs: any;
  constructor() {
    this.envs = {'M100': 'http://127.0.0.1:8991'};
  }

  getEnvs(alias: string): string {
    const f = '';
    console.log(this.c, f);
    console.log(this.c, f, 'alias', alias);
    const env = this.envs[alias];
    console.log(this.c, f, 'alias', alias, 'env', env);
    return env;
  }
}
