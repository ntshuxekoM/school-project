import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AllBlacklistedSitesComponent } from './all-blacklisted-sites.component';

describe('AllBlacklistedSitesComponent', () => {
  let component: AllBlacklistedSitesComponent;
  let fixture: ComponentFixture<AllBlacklistedSitesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AllBlacklistedSitesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AllBlacklistedSitesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
