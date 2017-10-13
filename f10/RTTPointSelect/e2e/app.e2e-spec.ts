import { RttPage } from './app.po';

describe('Rtt App', () => {
  let page: RttPage;

  beforeEach(() => {
    page = new RttPage();
  });

  it('should display welcome message', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('Welcome to app!!');
  });
});
