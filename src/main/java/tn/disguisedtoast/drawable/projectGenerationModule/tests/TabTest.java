package tn.disguisedtoast.drawable.projectGenerationModule.tests;

public class TabTest {
    public static void main(String[] args) {
        String routeTemplate = "{ path: '#tab', loadChildren: '../#tab/#tab.module##TabPageModule' },";
        String tabName = "page18-tab1";
        String route = routeTemplate.replace("#tab", tabName)
                .replace("#Tab", getReversePageName(tabName));
        System.out.println(route);
    }

    public static String getReversePageName(String pageName) {
        pageName = pageName.trim();
        for (int i = 0; i < pageName.length(); i++) {
            char c = pageName.charAt(i);
            if (i == 0) {
                c = (c + "").toUpperCase().charAt(0);
                pageName = c + pageName.substring(1);
            } else if (c == '-') {
                c = (pageName.charAt(i + 1) + "").toUpperCase().charAt(0);
                pageName = pageName.substring(0, i) + c + pageName.substring(i + 2);
            }
        }
        return pageName;
    }
}
