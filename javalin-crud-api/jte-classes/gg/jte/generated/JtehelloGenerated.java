package gg.jte.generated;
public final class JtehelloGenerated {
	public static final String JTE_NAME = "hello.jte";
	public static final int[] JTE_LINE_INFO = {0,0,0,0,5,5,5,5,6,6,6,8};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, app.HelloPage page, app.Localizer localizer) {
		jteOutput.writeContent("\n<html lang=\"en\">\n<body>\n    <p>");
		jteOutput.setContext("p", null);
		jteOutput.writeUserContent(localizer.localize("hello.visitor"));
		jteOutput.writeContent("</p>\n    <p>");
		jteOutput.setContext("p", null);
		jteOutput.writeUserContent(localizer.localize("hello.user-of-the-day", page.userName,page.userKarma));
		jteOutput.writeContent("</p>\n</body>\n</html>");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		app.HelloPage page = (app.HelloPage)params.get("page");
		app.Localizer localizer = (app.Localizer)params.get("localizer");
		render(jteOutput, jteHtmlInterceptor, page, localizer);
	}
}
