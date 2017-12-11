import { CsvToCardsPipe } from './csv-to-cards.pipe';

describe('CsvToCardsPipe', () => {
  it('create an instance', () => {
    const pipe = new CsvToCardsPipe();
    expect(pipe).toBeTruthy();
  });
});
