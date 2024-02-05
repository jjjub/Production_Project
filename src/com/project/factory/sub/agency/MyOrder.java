package com.project.factory.sub.agency;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.Scanner;

import com.project.factory.Main;
import com.project.factory.Today;
import com.project.factory.Toolkit;
import com.project.factory.member.Identify;
import com.project.factory.resource.Data;
import com.project.factory.resource.Members;
import com.project.factory.resource.Path;
import com.project.factory.resource.sub.Order;
import com.project.factory.resource.sub.OrderData;
import com.project.factory.view.MainView;
import com.project.factory.view.sub.MyOrderView;

public class MyOrder {

	static Scanner scan = new Scanner(System.in);

	private static String id; // 주문서 번호
	private static String agencyName; // 대리점명
	private static String agencyAddress; // 대리점 주소
	private static String agencyPhoneNum; // 대리점 전화번호
	private static int quantity; // 수량
	private static String dueDate; // 납기일
	private static String modelId; // 모델ID

	public static void myOrder() {

		OrderData.load();

		MyOrderView.myOrderMenu();
		Main.selectNum = scan.nextLine();

		if (Main.selectNum.equals("1")) {
			MyOrder.orderAdd();
		} else if (Main.selectNum.equals("2")) {
			MyOrder.orderEdit();
		} else if (Main.selectNum.equals("3")) {
			MyOrder.orderDelete();
		} else if (Main.selectNum.equals("4")) {
			MyOrder.orderView();
		} else {
			System.out.println();
			System.out.println("잘못된 번호입니다.");
			if (MainView.checkContinueBoolean()) {
				MyOrder.myOrder();
				return;
			} else {
				MainView.pause();
				return;
			}
		}
	}// MyOrder

	private static void orderAdd() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(Path.ORDER, true)); // true인 경우 이어쓰기

			while (true) {

				MyOrderView.orderAddMenu();
				Main.selectNum = scan.nextLine();

				if (MyOrder.checkModelExists()) { // MyOrder.modelId 설정

					if (MyOrder.writeQuantity()) {

						MyOrder.createOrderId(); // 주문서 번호 생성

						MyOrder.getAgencyInfo(); // 이름, 주소, 전화번호

						//myOrder() 메서드에서 처음에 OrderData.load()를 하면서 리스트에 불러오기 때문에 따로 리스트에 저장할 필요X
						// 주문서번호■주문서 작성일■대리점명■대리점 주소■전화번호■개수■납기일■모델명
						writer.write(
								MyOrder.id + "■" + 
								Today.day() + "■" + 
								MyOrder.agencyName + "■" + 
								MyOrder.agencyAddress + "■" + 
								MyOrder.agencyPhoneNum + "■" + 
								MyOrder.quantity + "■" + 
								Today.daysLater() + "■" + 
								MyOrder.modelId);
						writer.newLine();
						writer.close();

						System.out.println();
						System.out.println("주문이 성공적으로 완료되었습니다.");
						MainView.pauseToSel();

						MyOrder.myOrder();
						return;

					} else {
						MainView.pauseToSel();
						MyOrder.myOrder();
						return;
					}
				} else {
					System.out.println();
					System.out.println("잘못된 번호입니다.");
					if (MainView.checkContinueBoolean()) {
						continue;
					} else {
						MainView.pauseToSel();
						MyOrder.myOrder();
						return;
					}
				}
			}//while
		} catch (Exception e) {
			System.out.println("MyOrder.orderAdd");
			e.printStackTrace();
		}
	}//orderAdd
	
	private static void orderEdit() {
		while(true) {
			
		}//while
	}//orderEdit

	private static void orderDelete() {
		// TODO Auto-generated method stub

	}

	private static void orderView() {
		// TODO Auto-generated method stub

	}

	private static void getAgencyInfo() {
		for (Members member : Data.memberList) {
			if (member.getId().equals(Identify.auth)) { // 주문서 작성 중인 회원

				MyOrder.agencyName = member.getName();
				MyOrder.agencyAddress = member.getAddress();
				MyOrder.agencyPhoneNum = member.getPhoneNum();

				break;
			}
		}
	}

	private static boolean writeQuantity() {
		while (true) {
			System.out.print("원하는 수량을 입력하세요: ");
			Main.answer = scan.nextLine();

			if (!Main.answer.isEmpty()) { // 빈 문자열이 아니면
				if (Toolkit.isInteger(Main.answer)) { // 정수인지 확인

					MyOrder.quantity = Integer.parseInt(Main.answer);

					return true;

				} else {
					System.out.println();
					System.out.println("잘못된 입력입니다. 숫자를 입력해주세요.");
					if (MainView.checkContinueBoolean()) {
						continue;
					} else {
						return false;
					}
				}
			} else {
				System.out.println("잘못된 번호입니다.");
				if (MainView.checkContinueBoolean()) {
					continue;
				} else {
					return false;
				}
			}
		} // while
	}// writeQuantity

	// 유효성 검사
	private static boolean checkModelExists() {
		if (Main.selectNum.equals("1")) {
			MyOrder.modelId = "K3";
			return true;
		} else if (Main.selectNum.equals("2")) {
			MyOrder.modelId = "K5";
			return true;
		} else if (Main.selectNum.equals("3")) {
			MyOrder.modelId = "K7";
			return true;
		} else if (Main.selectNum.equals("4")) {
			MyOrder.modelId = "K9";
			return true;
		} else {
			return false;
		}
	}

	private static void createOrderId() {

		Random random = new Random(); // 랜덤 객체 생성

		while (true) {
			MyOrder.id = String.valueOf(100000 + random.nextInt(900000)); // 6자리 랜덤 숫자 (100000~999999)
			boolean isDuplicate = false;

			for (Order order : OrderData.orderList) {
				if (order.getId().equals(MyOrder.id)) {
					isDuplicate = true;
					break;
				}
			}

			if (!isDuplicate) {
				// 중복이 없으면 반복문 종료
				break;
			}
		}
	}// createOrderId

}
