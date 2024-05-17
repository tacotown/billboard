package org.mtk.arcade.billboard;

import java.io.IOException;

public class BoardFactory {

	public static BillboardLayer getLayer(String board, String romloc,
			boolean mini) throws IOException {
		int size = mini ? 16 : 32;

		switch (board) {
			case "martini":
				return new MartiniBillboardLayer(romloc, size);
			case "pepsi":
				return new PepsiBoardLayer(romloc, size);
			case "marlboro":
				return new MarlboroBillboardLayer(romloc, size);
			case "namco":
				return new NamcoAtariBillboardLayer(romloc, size);
			case "digdug":
				return new DigDugBillboardLayer(romloc, size);
			case "centipede":
				return new CentipedeBillboardLayer(romloc, size);
			default:
				return null;
		}
	}
}
