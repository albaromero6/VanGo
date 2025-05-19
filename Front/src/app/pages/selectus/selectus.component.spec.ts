import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SelectusComponent } from './selectus.component';

describe('SelectusComponent', () => {
  let component: SelectusComponent;
  let fixture: ComponentFixture<SelectusComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SelectusComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SelectusComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
