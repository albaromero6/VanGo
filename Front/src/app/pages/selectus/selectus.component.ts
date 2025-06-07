import { Component, OnInit, HostListener } from '@angular/core';

@Component({
  selector: 'app-selectus',
  standalone: true,
  imports: [],
  templateUrl: './selectus.component.html',
  styleUrl: './selectus.component.scss'
})
export class SelectusComponent implements OnInit {
  private animatedElements: HTMLElement[] = [];

  ngOnInit() {
    // Inicializar los elementos animables
    this.animatedElements = Array.from(document.querySelectorAll('.reveal-left, .reveal-top, .reveal-item')) as HTMLElement[];
    // Verificar elementos visibles al cargar la página
    this.checkVisibility();
  }

  @HostListener('window:scroll')
  onScroll() {
    this.checkVisibility();
  }

  private checkVisibility() {
    const triggerBottom = window.innerHeight * 0.8;

    this.animatedElements.forEach(element => {
      const elementTop = element.getBoundingClientRect().top;

      if (elementTop < triggerBottom) {
        element.classList.add('active');
      }
    });
  }
}
