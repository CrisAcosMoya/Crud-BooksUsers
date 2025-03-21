import { Component, AfterViewInit, ViewChild, ElementRef  } from '@angular/core';
import lottie from 'lottie-web';
import { Router } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [MatButtonModule, MatToolbarModule],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements AfterViewInit {

  constructor(private router: Router) {}

  goToAdd() {
    this.router.navigate(['/add']);
  }

  goToregister() {
    this.router.navigate(['/register']);
  }

  @ViewChild('lottieContainer', { static: false }) lottieContainer!: ElementRef;

  ngAfterViewInit(): void {
    lottie.loadAnimation({
      container: this.lottieContainer.nativeElement,
      renderer: 'svg',
      loop: true,
      autoplay: true,
      path: 'assets/Learned.json'
    });
  }
}
