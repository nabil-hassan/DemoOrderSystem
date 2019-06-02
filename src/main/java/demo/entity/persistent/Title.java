package demo.entity.persistent;

public enum Title {

	MR("Mr"), DR("Dr"), MRS("Mrs"), MISS("Miss"), MS("Ms");

	private String value;

	Title(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
