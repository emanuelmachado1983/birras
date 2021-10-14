import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdministrateMeetupComponent } from './administrate-meetup.component';

describe('AdministrateMeetupComponent', () => {
  let component: AdministrateMeetupComponent;
  let fixture: ComponentFixture<AdministrateMeetupComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdministrateMeetupComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdministrateMeetupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
