package tn.disguisedtoast.drawable.storyboardModule.tests;

import guru.nidi.graphviz.attribute.Image;
import guru.nidi.graphviz.attribute.Size;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.engine.GraphvizV8Engine;

import java.io.File;
import java.io.IOException;

import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;

public class TestGraphViz {
    public static void main(String[] args) throws IOException {
        /*Node
                main = node("main").with(Label.html("<b>main</b><br/>start"), Color.rgb("1020d0").font()),
                init = node(Label.markdown("**_init_**")),
                execute = node("execute"),
                compare = node("compare").with(Shape.RECTANGLE, Style.FILLED, Color.hsv(.7, .3, 1.0)),
                mkString = node("mkString").with(Label.lines(LEFT, "make", "a", "multi-line")),
                printf = node("printf");

        Graph g = graph("example2").directed().with(
                main.link(
                        to(node("parse").link(execute)).with(LinkAttr.weight(8)),
                        to(init).with(Style.DOTTED),
                        node(" ").with(Size.std().margin(.8, .7), Image.of("fb.png")),
                        to(printf).with(Style.BOLD, Label.of("100 times"), Color.RED)),
                execute.link(
                        graph().with(mkString, printf),
                        to(compare).with(Color.RED)),
                init.link(mkString));

        Graphviz.fromGraph(g).basedir(new File("example"))
                .render(Format.PNG).toFile(new File("example/ex3.png"));*/
        //String json = Graphviz.fromGraph(g).engine(Engine.NEATO).render(Format.JSON).toString();
        //FileUtils.writeStringToFile(new File("example/ex2.json"), json);

        Graphviz g = Graphviz.fromGraph(graph()
                .with(node(" ").with(Size.std().margin(.8, .7), Image.of("example/test.png"))));
        Graphviz.useEngine(new GraphvizV8Engine());
        g.render(Format.PNG).toFile(new File("example/ex4.png"));
    }
}
