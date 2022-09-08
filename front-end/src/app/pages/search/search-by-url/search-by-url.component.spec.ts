import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchByUrlComponent } from './search-by-url.component';

describe('SearchByUrlComponent', () => {
  let component: SearchByUrlComponent;
  let fixture: ComponentFixture<SearchByUrlComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SearchByUrlComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SearchByUrlComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
