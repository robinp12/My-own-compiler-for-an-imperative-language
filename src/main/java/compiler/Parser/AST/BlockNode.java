package compiler.Parser.AST;

import java.text.ParseException;
import java.util.ArrayList;

public class BlockNode extends ExpressionNode{
    //TODO
    public BlockNode() {
    }

    public static BlockNode parseBlock() throws ParseException {
        return new BlockNode();
    }
}