package PaintGUI;

public class FontSelect {

	public String select(String fName, boolean b, boolean i) {

		String font = "";

		switch (fName) {
		case "Arial":
			if (b) {
				if (i) {
					font = "Arial Bold Italic";
				} else {
					font = "Arial Bold";
				}
			} else if (i) {
				font = "Arial Italic";
			} else {
				font = "Arial";
			}
			break;
		case "Courier New":
			if (b) {
				if (i) {
					font = "Courier New Bold Italic";
				} else {
					font = "Courier New Bold";
				}
			} else if (i) {
				font = "Courier New Italic";
			} else {
				font = "Courier New";
			}
			break;
		case "Serif":
			if (b) {
				if (i) {
					font = "Serif Bold Italic";
				} else {
					font = "Serif Bold";
				}
			} else if (i) {
				font = "Serif Italic";
			} else {
				font = "Serif";
			}
			break;
		case "Times New Roman":
			if (b) {
				if (i) {
					font = "Times New Roman Bold Italic";
				} else {
					font = "Times New Roman Bold";
				}
			} else if (i) {
				font = "Times New Roman Italic";
			} else {
				font = "Times New Roman";
			}
			break;
		}

		return font;
	}
}
