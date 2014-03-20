package smartgrid.component;

public interface Fault {
	public double getValue(double value);
	public boolean isNull();
	public String name();
}
