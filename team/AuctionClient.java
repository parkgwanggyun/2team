package team;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class AuctionClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5004);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("경매서버 입장.");
            while (true) {
                System.out.println("1. 물품등록");
                System.out.println("2. 가격등록");
                System.out.println("3. 입찰하기");
                System.out.println("4. 최고입찰자 표기");
                System.out.println("5. 경매종료");
                System.out.print("옵션 선택 :  ");
                int option = scanner.nextInt();
                scanner.nextLine();  

                switch (option) {
                    case 1:
                        System.out.print("물품 등록 : ");
                        String itemName = scanner.nextLine();
                        System.out.print("시작 입찰가 등록: ");
                        double startingBid = scanner.nextDouble();
                        scanner.nextLine();  
                        out.println("추가된 물품 " + itemName + " " + startingBid);
                        break;
                    case 2:
                        System.out.print("경매자 이름 등록 : ");
                        String bidderName = scanner.nextLine();
                        out.println("경매자 " + bidderName);
                        break;
                    case 3:
                        System.out.print("경매자 이름 : ");
                        String bidder = scanner.nextLine();
                        System.out.print("물품 이름 : ");
                        String item = scanner.nextLine();
                        System.out.print("입찰 가격 등록 : ");
                        double bidAmount = scanner.nextDouble();
                        scanner.nextLine();  
                        out.println("입찰  " + bidder + " " + item + " " + bidAmount);
                        break;
                    case 4:
                        System.out.print("물품 검색: ");
                        String itemToCheck = scanner.nextLine();
                        out.println("최고가 " + itemToCheck);
                        break;
                    case 5:
                        out.println("경매 종료");
                        break;
                    default:
                        System.out.println("잘못된 옵션입니다. 다시 시도해주세요.");
                }

                String response = in.readLine();
                System.out.println(response);
                if (option == 5) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}