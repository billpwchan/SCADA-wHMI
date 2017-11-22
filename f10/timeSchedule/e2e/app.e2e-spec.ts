import { TscPage } from './app.po';

describe('tsc App', () => {
  let page: TscPage;

  beforeEach(() => {
    page = new TscPage();
  });

  it('should display welcome message', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('Welcome to app!!');
  });
});
