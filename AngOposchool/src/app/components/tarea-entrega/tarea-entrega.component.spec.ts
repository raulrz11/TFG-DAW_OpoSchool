import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TareaEntregaComponent } from './tarea-entrega.component';

describe('TareaEntregaComponent', () => {
  let component: TareaEntregaComponent;
  let fixture: ComponentFixture<TareaEntregaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TareaEntregaComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TareaEntregaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
