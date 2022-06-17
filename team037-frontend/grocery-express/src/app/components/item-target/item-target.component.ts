import { Component, OnInit } from '@angular/core';
import { Item } from 'src/app/common/item';
import { ItemService } from 'src/app/services/item.service';

@Component({
  selector: 'app-item-target',
  templateUrl: './item-target.component.html',
  styleUrls: ['./item-target.component.css']
})
export class ItemTargetComponent implements OnInit {

  items!: Item[];

  constructor(private itemService: ItemService) { }

  ngOnInit(): void {
    this.listItems();
  }

  listItems() {
    this.itemService.getTargetList().subscribe(
      (data) => {
        console.log(data); // TODO: Delete this line
        this.items = data;
      }
    )
  }

}
