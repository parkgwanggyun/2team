package team;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date; // java.util.Date로 수정
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

class AuctionItem {
    String name;
    double startingBid;
    double highestBid;
    String highestBidder;
    Date endTime; // 경매 종료 시간
    Timer timer; // 타이머 객체

    AuctionItem(String name, double startingBid, int auctionTimeSeconds) {
        this.name = name;
        this.startingBid = startingBid;
        this.highestBid = startingBid;
        this.highestBidder = "아직입찰자가 없습니다.";
        this.endTime = new Date(System.currentTimeMillis() + auctionTimeSeconds * 1000); // 경매 종료 시간 설정
        this.timer = new Timer();
        this.timer.schedule(new AuctionEndTask(this), this.endTime); // 수정된 부분
    }
}

class AuctionEndTask extends TimerTask {
    private AuctionItem item;

    AuctionEndTask(AuctionItem item) {
        this.item = item;
    }

    public void run() {
        System.out.println("경매 물품 '" + item.name + "' 최고 입찰자 : " + item.highestBidder + " 최고 입찰가 : " + item.highestBid);
        item.timer.cancel(); // 타이머 종료
    }
}

class Auction {
    List<AuctionItem> items = new ArrayList<>();
    List<String> bidders = new ArrayList<>();

    void addItem(String name, double startingBid, int auctionTimeSeconds) {
        AuctionItem item = new AuctionItem(name, startingBid, auctionTimeSeconds);
        items.add(item);
        System.out.println("추가된 아이템 '" + name + "' 시작 입찰가 : " + startingBid + " 경매 시간 " + auctionTimeSeconds + " 초");
    }

    void addBidder(String name) {
        bidders.add(name);
        System.out.println("추가 입찰자  '" + name + "'");
    }

    String placeBid(String bidderName, String itemName, double bidAmount) {
        for (AuctionItem item : items) {
            if (item.name.equals(itemName)) {
                if (bidAmount > item.highestBid) {
                    item.highestBid = bidAmount;
                    item.highestBidder = bidderName;
                    return bidderName + " 입찰 " + bidAmount + " 에 " + itemName;
                } else {
                    return "입찰가가 현재 최고 입찰가 보다 낮습니다..";
                }
            }
        }
        return "항목을 찾을수 없습니다.";
    }

    String showHighestBidder(String itemName) {
        for (AuctionItem item : items) {
            if (item.name.equals(itemName)) {
                return "최고 입찰자 " + itemName + " 입니다. " + item.highestBidder + " 입찰가는 " + item.highestBid;
            }
        }
        return "항목을 찾을수 없습니다.";
    }

    String endAuction(String itemName) {
        for (AuctionItem item : items) {
            if (item.name.equals(itemName)) {
                item.timer.cancel(); // 타이머 종료
                return "경매 품목 : '" + itemName + "' 최종 결과: 최고 입찰자는 다음과 같습니다. " + item.highestBidder + " 입찰가로 " + item.highestBid;
            }
        }
        return "항목을 찾을수 없습니다..";
    }
}

public class AuctionServer {
    private static Auction auction = new Auction();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5004)) {
            System.out.println("경매서버 연결중...");
            while (true) {
                new ClientHandler(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    String[] parts = inputLine.split(" ");
                    String command = parts[0];
                    String result;
                    switch (command) {
                        case "ADD_ITEM":
                            String itemName = parts[1];
                            double startingBid = Double.parseDouble(parts[2]);
                            int auctionTimeSeconds = Integer.parseInt(parts[3]);
                            auction.addItem(itemName, startingBid, auctionTimeSeconds);
                            out.println("Item added.");
                            break;
                        case "ADD_BIDDER":
                            String bidderName = parts[1];
                            auction.addBidder(bidderName);
                            out.println("Bidder added.");
                            break;
                        case "PLACE_BID":
                            bidderName = parts[1];
                            itemName = parts[2];
                            double bidAmount = Double.parseDouble(parts[3]);
                            result = auction.placeBid(bidderName, itemName, bidAmount);
                            out.println(result);
                            break;
                        case "SHOW_HIGHEST_BIDDER":
                            itemName = parts[1];
                            result = auction.showHighestBidder(itemName);
                            out.println(result);
                            break;
                        case "END_AUCTION":
                            itemName = parts[1];
                            result = auction.endAuction(itemName);
                            out.println(result);
                            break;
                        default:
                            out.println("알 수 없는 명령어.");
                            break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
