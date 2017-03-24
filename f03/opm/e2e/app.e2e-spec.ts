import { FasOpmPage } from './app.po';

describe('fas-opm App', () => {
  let page: FasOpmPage;

  beforeEach(() => {
    page = new FasOpmPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
