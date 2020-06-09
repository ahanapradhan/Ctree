package petrinetmodel;

public class Place extends Node {

	public Place(String s) {
		super(s.toLowerCase());
	}

	/*@Override
	public String buildECWS() {
		// only place can be source or sink
        StringBuilder sb = new StringBuilder();
		sb.append(this.getLabel() + " ");
		
		if (this.getOutArcs() != null && this.getOutArcs().size() == 1) {
			sb.append(getOutArcs().get(0).getOutNode().buildECWS());
		}
		return sb.toString();	
	}*/

	@Override
	protected String getOpen() {
		return "\\[ ";
	}

	@Override
	protected String getClose() {
		return "\\] ";
	}

}
