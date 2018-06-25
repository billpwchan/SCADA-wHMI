import { Injectable } from '@angular/core';

@Injectable()
export class EnvironmentMappingService {

  readonly c = 'EnvironmentMappingService';

  private envs: {} = {};
  constructor() { }

  setEnv(alias: string, address: string): void {
    const f = 'setEnv';
    console.log(this.c, f);
    console.log(this.c, f, 'alias', alias, 'address', address);
    this.envs[alias] = address;
  }

  getEnv(alias: string): string {
    const f = 'getEnv';
    console.log(this.c, f);
    console.log(this.c, f, 'alias', alias);
    const env = this.envs[alias];
    console.log(this.c, f, 'alias', alias, 'env', env);
    return env;
  }
}
