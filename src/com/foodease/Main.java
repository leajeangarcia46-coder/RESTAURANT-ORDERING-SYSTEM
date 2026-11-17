package com.foodease;

import com.foodease.application.OrderService;
import com.foodease.console.ConsoleApp;
import com.foodease.console.ConsoleInput;
import com.foodease.console.ConsoleOutput;
import com.foodease.data.InMemoryMenuRepository;

public class Main {
    public static void main(String[] args) {
        ConsoleInput input = new ConsoleInput();
        ConsoleOutput output = new ConsoleOutput();
        OrderService orderService = new OrderService(new InMemoryMenuRepository());
        new ConsoleApp(input, output, orderService).run();
    }
}
