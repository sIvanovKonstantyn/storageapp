package com.home.kivanov.examples;

import com.home.kivanov.examples.documents.GoodsArrival;
import com.home.kivanov.examples.documents.GoodsShipment;
import com.home.kivanov.examples.documents.Inventory;
import com.home.kivanov.examples.goods.Goods;
import com.home.kivanov.examples.goods.StorageItem;
import com.home.kivanov.examples.services.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class StorageApplication {


    public void run() {

        System.out.println("******************************************************");
        System.out.println("******************Storage application*****************");
        printMenu();

        final StorageService storageService = new StorageServiceImpl();
        final GoodsArrivalService goodsArrivalService = new GoodsArrivalServiceImpl(storageService);

        final Scanner scanner = new Scanner(System.in);
        String command = null;

        while (!"0".equals(command)) {
            command = scanner.nextLine();

            switch (command) {
                case "1": {
                    GoodsArrival goodsArrival = new GoodsArrival(
                            1L,
                            storageService,
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
                            storageService,
                            "GS001",
                            LocalDateTime.now(),
                            Collections.singletonList(
                                    takeItemFromInput()
                            )
                    );

                    final GoodsShipmentService goodsShipmentService = new GoodsShipmentServiceImpl(storageService);

                    goodsShipmentService.takeGoodsFromStorageByGoodsShipment(goodsShipment);
                    System.out.println(goodsShipment.getGoods() + " has taken");
                    break;
                }
                case "3": {
                    Inventory inventory = new Inventory(
                            1L,
                            storageService,
                            "IN001",
                            LocalDateTime.now(),
                            Collections.singletonList(
                                    takeItemFromInput()
                            )
                    );

                    InventoryService inventoryService = new InventoryServiceImpl(storageService);
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
        final int count = Integer.parseInt(scanner.nextLine());

        return new StorageItem(
                new Random().nextLong(),
                new Goods(goodsName, goodsDescription),
                count

        );
    }
}
