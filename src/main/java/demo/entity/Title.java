package demo.entity;

public enum Title {

	MR("Mr"), DR("Dr"), MRS("Mrs"), MISS("Miss"), MS("Ms");

	private String value;

	Title(String value) {
		this.value = value;
	}
}
