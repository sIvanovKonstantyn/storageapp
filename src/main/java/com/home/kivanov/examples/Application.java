package com.home.kivanov.examples;

import com.home.kivanov.examples.documents.GoodsArrival;
import com.home.kivanov.examples.documents.GoodsShipment;
import com.home.kivanov.examples.documents.Inventory;
import com.home.kivanov.examples.documents.Storage;
import com.home.kivanov.examples.goods.Goods;
import com.home.kivanov.examples.goods.StorageItem;
import com.home.kivanov.examples.services.GoodsArrivalService;
import com.home.kivanov.examples.services.GoodsShipmentService;
import com.home.kivanov.examples.services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

@Component
public class Application {

    private final GoodsArrivalService goodsArrivalService;
    private final GoodsShipmentService goodsShipmentService;
    private final InventoryService inventoryService;

    @Autowired
    public Application(GoodsArrivalService goodsArrivalService, GoodsShipmentService goodsShipmentService, InventoryService inventoryService) {
        this.goodsArrivalService = goodsArrivalService;
        this.goodsShipmentService = goodsShipmentService;
        this.inventoryService = inventoryService;
    }

    public void run() {

        System.out.println("******************************************************");
        System.out.println("******************Storage application*****************");
        printMenu();


        final Scanner scanner = new Scanner(System.in);
        String command = null;

        while (!"0".equals(command)) {
            command = scanner.nextLine();

            switch (command) {
                case "1": {
                    GoodsArrival goodsArrival = new GoodsArrival(
                            1L,
                            new Storage(1L, "Single storage"),
                            "GA001",
                            LocalDateTime.now(),
                            Collections.singletonList(
                                    takeItemFromInput()
                            )
                    );

                    goodsArrivalService.addGoodsToStorageByGoodsArrival(goodsArrival);
                    System.out.println(goodsArrival.getGoods() + " has added");
                    break;
                }
                case "2": {
                    GoodsShipment goodsShipment = new GoodsShipment(
                            1L,
                            new Storage(1L, "Single storage"),
                            "GS001",
                            LocalDateTime.now(),
                            Collections.singletonList(
                                    takeItemFromInput()
                            )
                    );


                    goodsShipmentService.takeGoodsFromStorageByGoodsShipment(goodsShipment);
                    System.out.println(goodsShipment.getGoods() + " has taken");
                    break;
                }
                case "3": {
                    Inventory inventory = new Inventory(
                            1L,
                            new Storage(1L, "Single storage"),
                            "IN001",
                            LocalDateTime.now(),
                            Collections.singletonList(
                                    takeItemFromInput()
                            )
                    );

                    System.out.println("All goods: " + inventoryService.calculate());
                    System.out.println("inventory result: " + inventoryService.calculate(inventory));
                }
            }

            printMenu();
        }
    }

    private void printMenu() {
        System.out.println("******************************************************");
        System.out.println();
        System.out.println("--(1) Add goods");
        System.out.println("--(2) Take goods");
        System.out.println("--(3) Create inventory");
        System.out.println();
        System.out.println("--(0) exit");
    }

    private StorageItem takeItemFromInput() {

        final Scanner scanner = new Scanner(System.in);

        System.out.println("Enter a goods name");
        final String goodsName = scanner.nextLine();

        System.out.println("Enter a goods description");
        final String goodsDescription = scanner.nextLine();

        System.out.println("Enter a count");
        final Long count = Long.parseLong(scanner.nextLine());

        return new StorageItem(
                new Random().nextLong(),
                new Goods(goodsName, goodsDescription),
                count

        );
    }
}
