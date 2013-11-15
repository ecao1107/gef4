package org.eclipse.gef4.mvc.javafx.example;

import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import org.eclipse.gef4.mvc.aspects.dragging.IDragPolicy;
import org.eclipse.gef4.mvc.aspects.selection.ISelectionPolicy;
import org.eclipse.gef4.mvc.javafx.AbstractFXNodeEditPart;
import org.eclipse.gef4.mvc.javafx.FXDragPolicy;
import org.eclipse.gef4.mvc.javafx.FXSelectionPolicy;

public class FXExampleNodeEditPart extends AbstractFXNodeEditPart {

	private Rectangle visual;

	public FXExampleNodeEditPart() {
		visual =  new Rectangle();
		visual.setFill(Color.RED);
		installEditPolicy(ISelectionPolicy.class, new FXSelectionPolicy());
		installEditPolicy(IDragPolicy.class, new FXDragPolicy());
	}
	
	@Override
	public Rectangle2D getModel() {
		return (Rectangle2D)super.getModel();
	}

	@Override
	public Node getVisual() {
		return visual;
	}

	@Override
	public void refreshVisual() {
		visual.setLayoutX(getModel().getMinX());
		visual.setLayoutY(getModel().getMinY());
		visual.setWidth(getModel().getWidth());
		visual.setHeight(getModel().getHeight());
	}

	@Override
	protected boolean isNodeModel(Object model) {
		return true;
	}

	@Override
	protected boolean isConnectionModel(Object model) {
		return false;
	}

}