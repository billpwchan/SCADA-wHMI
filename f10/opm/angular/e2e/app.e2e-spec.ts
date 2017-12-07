import { OpmCliPage } from './app.po';

describe('opm-cli App', () => {
  let page: OpmCliPage;

  beforeEach(() => {
    page = new OpmCliPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
