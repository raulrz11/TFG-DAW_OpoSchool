import { TestBed } from '@angular/core/testing';

import { SubscripcionesService } from './subscripciones.service';

describe('SubscripcionesService', () => {
  let service: SubscripcionesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SubscripcionesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
