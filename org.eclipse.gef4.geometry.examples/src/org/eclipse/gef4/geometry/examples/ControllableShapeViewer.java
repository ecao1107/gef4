package org.eclipse.gef4.geometry.examples;

import java.util.ArrayList;

import org.eclipse.gef4.geometry.planar.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class ControllableShapeViewer implements PaintListener, MouseListener,
		MouseMoveListener, Listener {

	public Canvas canvas;
	private ArrayList<ControllableShape> shapes = new ArrayList<ControllableShape>();
	private boolean isDragging = false;
	private ControllableShape draggedShape;
	private int dragPointIndex;

	public ControllableShapeViewer(Canvas pCanvas) {
		canvas = pCanvas;
		canvas.addPaintListener(this);
		canvas.addMouseListener(this);
		canvas.addMouseMoveListener(this);
		canvas.addListener(SWT.Resize, this);
	}

	public void addShape(ControllableShape shape) {
		shapes.add(shape);
	}

	public ControllableShape[] getShapes() {
		return shapes.toArray(new ControllableShape[] {});
	}

	public void clearShapes() {
		shapes.clear();
	}

	public void removeShape(int index) {
		shapes.remove(index);
	}

	public void paintControl(PaintEvent e) {
		for (ControllableShape shape : shapes) {
			e.gc.setForeground(canvas.getDisplay().getSystemColor(
					shape.shapeColor));
			e.gc.setBackground(canvas.getDisplay().getSystemColor(
					shape.shapeColor));

			shape.onDraw(e.gc);

			if (shape.isActive()) {
				e.gc.setForeground(canvas.getDisplay().getSystemColor(
						shape.controlColor));
				e.gc.setBackground(canvas.getDisplay().getSystemColor(
						shape.controlColor));

				for (ControlPoint cp : shape.controlPoints) {
					e.gc.fillOval((int) (cp.getX() - shape.controlRadius),
							(int) (cp.getY() - shape.controlRadius),
							(int) (shape.controlRadius * 2),
							(int) (shape.controlRadius * 2));
				}
			}
		}
	}

	public void mouseMove(MouseEvent e) {
		if (isDragging) {
			double oldX = draggedShape.controlPoints.get(dragPointIndex).getX();
			double oldY = draggedShape.controlPoints.get(dragPointIndex).getY();
			draggedShape.controlPoints.get(dragPointIndex).setX(e.x);
			draggedShape.controlPoints.get(dragPointIndex).setY(e.y);

			draggedShape.onMove(dragPointIndex, oldX, oldY);

			canvas.redraw();
		}
	}

	public void mouseDoubleClick(MouseEvent e) {
	}

	public void mouseDown(MouseEvent e) {
		for (ControllableShape cs : shapes) {
			if (cs.isActive()) {
				for (int i = 0; i < cs.controlPoints.size(); i++) {
					ControlPoint cp = cs.controlPoints.get(i);
					double dx = cp.getX() - e.x;
					double dy = cp.getY() - e.y;
					if (dx * dx + dy * dy < cs.controlRadius * cs.controlRadius) {
						isDragging = true;
						draggedShape = cs;
						dragPointIndex = i;
						return;
					}
				}
			}
		}
	}

	public void mouseUp(MouseEvent e) {
		isDragging = false;
	}

	public void handleEvent(Event e) {
		switch (e.type) {
		case SWT.Resize:
			Rectangle bounds = new Rectangle(canvas.getBounds());

			for (ControllableShape cs : shapes)
				cs.onResize(bounds);

			canvas.redraw();
			break;
		}
	}

}