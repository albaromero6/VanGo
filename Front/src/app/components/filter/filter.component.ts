import { Component, Input, Output, EventEmitter, signal, effect } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-filter',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './filter.component.html',
  styleUrls: ['./filter.component.scss']
})
export class FilterComponent {
  @Input() set currentSort(value: 'asc' | 'desc') {
    this.sortOrder.set(value);
  }
  @Output() sortChange = new EventEmitter<'asc' | 'desc'>();

  sortOrder = signal<'asc' | 'desc'>('asc');

  toggleSort() {
    this.sortOrder.update(current => current === 'asc' ? 'desc' : 'asc');
    this.sortChange.emit(this.sortOrder());
  }
}
