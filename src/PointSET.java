import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class PointSET {

	private Set<Point2D> container;

	public PointSET() {
		container = new TreeSet<>();
	} // construct an empty set of points

	public boolean isEmpty() {
		return container.isEmpty();
	} // is the set empty?

	public int size() {
		return container.size();
	} // number of points in the set

	public void insert(Point2D p) {
		container.add(p);
	} // add the point to the set (if it is not already in the set)

	public boolean contains(Point2D p) {
		return container.contains(p);
	} // does the set contain point p?

	public void draw() {
		for (Point2D point : container) {
			point.draw();
		}
	} // draw all points to standard draw

	public Iterable<Point2D> range(RectHV rect) {
		List<Point2D> list = new ArrayList<>();
		for (Point2D p : container) {
			if (rect.contains(p)) {
				list.add(p);
			}
		}
		return list;
	} // all points that are inside the rectangle (or on the boundary)

	public Point2D nearest(Point2D p) {
		Double max = Double.POSITIVE_INFINITY;
		Point2D ans = null;
		for (Point2D point2d : container) {
			double dist = p.distanceSquaredTo(point2d);
			if (max > dist) {
				max = dist;
				ans = point2d;
			}
		}
		return ans;
	} // a nearest neighbor in the set to point p; null if the set is empty

	public static void main(String[] args) {
	} // unit testing of the methods (optional)
}