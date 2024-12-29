import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubscripcionesComponent } from './subscripciones.component';

describe('SubscripcionesComponent', () => {
  let component: SubscripcionesComponent;
  let fixture: ComponentFixture<SubscripcionesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SubscripcionesComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SubscripcionesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
