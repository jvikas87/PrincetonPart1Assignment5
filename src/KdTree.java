
import java.util.Set;
import java.util.TreeSet;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

	private int size;

	private Node root;

	public KdTree() {
	} // construct an empty set of points

	public boolean isEmpty() {
		return size == 0;
	} // is the set empty?

	public int size() {
		return size;
	} // number of points in the set

	public void insert(Point2D p) {
		if(p==null) {
			throw new IllegalArgumentException();
		}
		Rectangle rectangle = new Rectangle();
		rectangle.xmin = 0;
		rectangle.xmax = 1;
		rectangle.ymin = 0;
		rectangle.ymax = 1;
		if (!contains(p)) {
			root = insert(root, p, rectangle, true);
			size++;
		}
	} // add the point to the set (if it is not already in the set)

	private Node insert(Node root, Point2D p, Rectangle rectangle, boolean flag) {
		if (root == null) {
			RectHV rectHV;
			if (flag) {
				rectHV = new RectHV(p.x(), rectangle.ymin, p.x(), rectangle.ymax);
			} else {
				rectHV = new RectHV(rectangle.xmin, p.y(), rectangle.xmax, p.y());
			}
			return new Node(p, rectHV, null, null);
		}
		if (flag) {
			if (p.x() < root.p.x()) {
				rectangle.xmax = root.p.x();
				root.lb = insert(root.lb, p, rectangle, !flag);
			} else {
				rectangle.xmin = root.p.x();
				root.rt = insert(root.rt, p, rectangle, !flag);
			}

		} else {
			if (p.y() < root.p.y()) {
				rectangle.ymax = root.p.y();
				root.lb = insert(root.lb, p, rectangle, !flag);
			} else {
				rectangle.ymin = root.p.y();
				root.rt = insert(root.rt, p, rectangle, !flag);
			}
		}
		return root;
	}

	public boolean contains(Point2D p) {
		if(p==null) {
			throw new IllegalArgumentException();
		}
		return contains(p, root, true);
	} // does the set contain point p?

	private boolean contains(Point2D p, Node root, boolean flag) {
		if (root == null) {
			return false;
		}
		if (root.p == p || (p.x() == root.p.x() && p.y() == root.p.y())) {
			return true;
		}
		if (flag) {
			if (p.x() < root.p.x()) {
				return contains(p, root.lb, !flag);
			} else {
				return contains(p, root.rt, !flag);
			}

		} else {
			if (p.y() < root.p.y()) {
				return contains(p, root.lb, !flag);
			} else {
				return contains(p, root.rt, !flag);
			}
		}
	}

	public void draw() {
		RectHV container = new RectHV(0, 1, 0, 1);
		container.draw();

		draw(root, true);
	} // draw all points to standard draw

	private void draw(Node root, boolean flag) {
		if (root == null) {
			return;
		}
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setPenRadius(0.01);
		root.p.draw();
		StdDraw.setPenRadius(0);
		if (flag) {
			StdDraw.setPenColor(StdDraw.RED);
		} else {
			StdDraw.setPenColor(StdDraw.BLUE);
		}
		root.rect.draw();
		draw(root.lb, !flag);
		draw(root.rt, !flag);
	}

	public Iterable<Point2D> range(RectHV rect) {
		if(rect==null) {
			throw new IllegalArgumentException();
		}
		Set<Point2D> list = new TreeSet<Point2D>();
		range(rect, root, true, list);
		return list;
	} // all points that are inside the rectangle (or on the boundary)

	private void range(RectHV rect, Node root, boolean flag, Set<Point2D> list) {
		if (root == null) {
			return;
		}
		if (rect.contains(root.p)) {
			list.add(root.p);
		}
		if (rect.intersects(root.rect)) {
			range(rect, root.lb, !flag, list);
			range(rect, root.rt, !flag, list);
		} else if (flag) {
			if (rect.xmax() < root.rect.xmin()) {
				range(rect, root.lb, !flag, list);
			} else if (rect.xmin() > root.rect.xmax()) {
				range(rect, root.rt, !flag, list);
			}
		} else {
			if (rect.ymax() < root.rect.ymin()) {
				range(rect, root.lb, !flag, list);
			} else if (rect.ymin() > root.rect.ymax()) {
				range(rect, root.rt, !flag, list);
			}
		}

	}

	public Point2D nearest(Point2D p) {
		if(p==null) {
			throw new IllegalArgumentException();
		}
		DistancePointWrapper wrapper = new DistancePointWrapper();
		wrapper.distance = Double.POSITIVE_INFINITY;
		nearest(p, root, true, wrapper);
		return wrapper.point2d;
	} // a nearest neighbor in the set to point p; null if the set is empty

	private Point2D nearest(Point2D p, Node root, boolean flag, DistancePointWrapper wrapper) {
		// TODO Auto-generated method stub
		if (root == null) {
			return null;
		}
		double distanceSqr = p.distanceSquaredTo(root.p);
		if (wrapper.distance > distanceSqr) {
			wrapper.distance = distanceSqr;
			wrapper.point2d = root.p;
		}
		Point2D point2d = null;
		if (flag) {
			if (p.x() < root.p.x()) {
				point2d = nearest(p, root.lb, !flag, wrapper);
				if (point2d == null) {
					point2d = nearest(p, root.rt, !flag, wrapper);
				}
			} else {
				point2d = nearest(p, root.rt, !flag, wrapper);
				if (point2d == null) {
					point2d = nearest(p, root.lb, !flag, wrapper);
				}
			}
		} else {
			if (p.y() < root.p.y()) {
				point2d = nearest(p, root.lb, !flag, wrapper);
				if (point2d == null) {
					point2d = nearest(p, root.rt, !flag, wrapper);
				}
			} else {
				point2d = nearest(p, root.lb, !flag, wrapper);
				if (point2d == null) {
					point2d = nearest(p, root.rt, !flag, wrapper);
				}
			}

		}
		return point2d;

	}

	public static void main(String[] args) {
		/*
		 * 
		 * Scanner scanner = new Scanner(new File("kdtree/input10.txt")); KdTree kdTree
		 * = new KdTree(); while (scanner.hasNextLine()) { String[] array =
		 * scanner.nextLine().split(" "); kdTree.insert(new
		 * Point2D(Double.parseDouble(array[0]), Double.parseDouble(array[1]))); }
		 * kdTree.draw();
		 */}

	private static class Node {
		public Node(Point2D p, RectHV rect, Node lb, Node rt) {
			this.p = p;
			this.rect = rect;
			this.lb = lb;
			this.rt = rt;
		}

		private Point2D p; // the point
		private RectHV rect; // the axis-aligned rectangle corresponding to this node
		private Node lb; // the left/bottom subtree
		private Node rt; // the right/top subtree
	}

	private class Rectangle {
		private double xmin;
		private double ymin;
		private double xmax;
		private double ymax;
	}

	private class DistancePointWrapper {
		private Point2D point2d;
		private double distance;
	}
}
