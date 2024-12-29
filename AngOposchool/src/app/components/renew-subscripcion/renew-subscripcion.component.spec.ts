import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RenewSubscripcionComponent } from './renew-subscripcion.component';

describe('RenewSubscripcionComponent', () => {
  let component: RenewSubscripcionComponent;
  let fixture: ComponentFixture<RenewSubscripcionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RenewSubscripcionComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RenewSubscripcionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
