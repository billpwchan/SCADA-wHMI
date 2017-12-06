import { AutologoutPage } from './app.po';

describe('autologout App', () => {
  let page: AutologoutPage;

  beforeEach(() => {
    page = new AutologoutPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
