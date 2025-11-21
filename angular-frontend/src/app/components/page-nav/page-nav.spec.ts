import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PageNav } from './page-nav';

describe('PageNav', () => {
  let component: PageNav;
  let fixture: ComponentFixture<PageNav>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PageNav]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PageNav);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
