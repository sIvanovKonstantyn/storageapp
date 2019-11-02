package com.home.kivanov.examples;

import com.home.kivanov.examples.documents.GoodsArrival;
import com.home.kivanov.examples.documents.GoodsShipment;
import com.home.kivanov.examples.documents.Inventory;
import com.home.kivanov.examples.goods.Goods;
import com.home.kivanov.examples.goods.StorageItem;
import com.home.kivanov.examples.services.StorageService;
import com.home.kivanov.examples.services.StorageServiceImpl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class StorageApplication {


    public void run() {

        System.out.println("******************************************************");
        System.out.println("******************Storage application*****************");
        printMenu();

        final StorageService storage = new StorageServiceImpl();

        final Scanner scanner = new Scanner(System.in);
        String command = null;

        while (!"0".equals(command)) {
            command = scanner.nextLine();

            switch (command) {
                case "1": {
                    GoodsArrival goodsArrival = new GoodsArrival(
                            storage,
                            "GA001",
                            LocalDateTime.now(),
                            Collections.singletonList(
                                    takeItemFromInput()
                            )
                    );

                    goodsArrival.addToStorage();
                    System.out.println(goodsArrival.getGoods() + " has added");
                    break;
                }
                case "2": {
                    GoodsShipment goodsShipment = new GoodsShipment(
                            storage,
                            "GS001",
                            LocalDateTime.now(),
                            Collections.singletonList(
                                    takeItemFromInput()
                            )
                    );

                    goodsShipment.takeFromStorage();
                    System.out.println(goodsShipment.getGoods() + " has taken");
                    break;
                }
                case "3": {
                    Inventory inventory = new Inventory(
                            storage,
                            "IN001",
                            LocalDateTime.now(),
                            Collections.singletonList(
                                    takeItemFromInput()
                            )
                    );

                    List<StorageItem> inventoryResultList = inventory.calculate();
                    System.out.println("inventory result: " + inventoryResultList);
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
