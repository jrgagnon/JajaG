package PaintGUI;
import javafx.scene.image.Image;

/** Stack of Images
 */
public class Stack {
	
	Node head;
	int size;

	Stack(){ // Probably don't need here: GraphicsContext gc, Canvas cv
		head = null;
		size = 0;
	}//end constructor

	/** Add new image to stack
	 */
	void push(Image im){
		size++;
		if(head == null){
			head = new Node(im, null);
			return;
		}
		Node n = new Node(im, head);
		head = n;
	}
	
	/** Return most recent image
	 */
	Image pop(){
		if(head == null)
			return null;
		Image ret = head.im;
		head = head.next;
		size--;
		return ret;
	}
	
	/** Stack Node
	 */
	class Node{
		
		Image im;
		Node next;
		
		Node(Image im, Node next){
			this.im = im;
			this.next = next;
		}
	}

}//end Stack class

/* Declare the canvas
JFXCanvas jc = new JFXCanvas();
Canvas canvas = new Canvas(cw, ch);
GraphicsContext gc = canvas.getGraphicsContext2D();
jc.draw(gc, canvas);
*/

/* Open file option
BufferedImage bufferedImage = ImageIO.read(file);
Image image = SwingFXUtils.toFXImage(bufferedImage, null);
jc.imageDraw(gc, canvas, image);
*/

/* Save feature
WritableImage writableImage = new WritableImage((int) cw, (int) ch);
						canvas.snapshot(null, writableImage);
						RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
						ImageIO.write(renderedImage, "png", file);
*/