package compiler.Bytecode;

import compiler.Lexer.SymbolKind;
import compiler.Parser.AST.*;

public class ExpressionEvaluator implements ASTevaluator {

    public ExpressionEvaluator(BinaryExpressionNode p) throws Exception {
        visit(p);
    }

    @Override
    public void visit(BinaryExpressionNode node) throws Exception {

        // Get the types of the left and right operands
        ExpressionNode left = node.getLeft();
        ExpressionNode right = node.getRight();

        if (left.getTypeStr().equals("binaryExp")) {
            visit((BinaryExpressionNode) node.getLeft());
        }
        if (right.getTypeStr().equals("binaryExp")) {
            visit((BinaryExpressionNode) node.getRight());
        }

        // Determine the result type based on the operator
        SymbolKind operatorKind = node.getOperator().getKind();
        switch (node.getResultType()) {
            case "str":
                LiteralNode lstr = (LiteralNode) left;
                LiteralNode rstr = (LiteralNode) right;

                if (operatorKind.equals(SymbolKind.PLUS)) {
                    node.setResult(lstr.getLiteral() + rstr.getLiteral());
                } else {
                    throw new Exception("Invalid operation: binary expression error type");
                }
                break;

            case "int":
                NumberNode lint = (NumberNode) left;
                NumberNode rint = (NumberNode) right;

                if (operatorKind.equals(SymbolKind.PLUS)){
                    node.setResult(String.valueOf(Integer.valueOf(lint.getValue()) + Integer.valueOf(rint.getValue())));
                }
                else if (operatorKind.equals(SymbolKind.MINUS)){
                    node.setResult(String.valueOf(Integer.valueOf(lint.getValue()) - Integer.valueOf(rint.getValue())));
                }
                else if (operatorKind.equals(SymbolKind.STAR)){
                    node.setResult(String.valueOf(Integer.valueOf(lint.getValue()) * Integer.valueOf(rint.getValue())));
                }
                else if (operatorKind.equals(SymbolKind.SLASH)){
                    node.setResult(String.valueOf(Integer.valueOf(lint.getValue()) / Integer.valueOf(rint.getValue())));
                }
                else if (operatorKind.equals(SymbolKind.PERC)){
                    node.setResult(String.valueOf(Integer.valueOf(lint.getValue()) % Integer.valueOf(rint.getValue())));
                }
                else {
                    throw new Exception("Invalid operation: binary expression error type");
                }
                break;


            case "real":
                NumberNode lcast = (NumberNode) left;
                NumberNode rcast = (NumberNode) right;
                Float lreal = Float.valueOf(lcast.getValue());
                Float rreal = Float.valueOf(rcast.getValue());

                if (operatorKind.equals(SymbolKind.PLUS)){
                    node.setResult(String.valueOf(lreal + rreal));
                }
                else if (operatorKind.equals(SymbolKind.MINUS)){
                    node.setResult(String.valueOf(lreal - rreal));
                }
                else if (operatorKind.equals(SymbolKind.STAR)){
                    node.setResult(String.valueOf(lreal * rreal));
                }
                else if (operatorKind.equals(SymbolKind.SLASH)){
                    node.setResult(String.valueOf(lreal / rreal));
                }
                else {
                    throw new Exception("Invalid operation: binary expression error type");
                }
                break;

            case "boolean":
                if (left.getTypeStr().equals("boolean")){
                    BooleanNode lb = (BooleanNode) left;
                    BooleanNode rb = (BooleanNode) right;
                    if (operatorKind.equals(SymbolKind.EQEQ)) {
                        node.setResult(String.valueOf(lb.isVal() == rb.isVal()));
                    } else if (operatorKind.equals(SymbolKind.DIFF)) {
                        node.setResult(String.valueOf(lb.isVal() != rb.isVal()));
                    } else {
                        throw new Exception("Invalid operation: binary expression error type");
                    }
                    //TODO: AND et OR
                    break;
                } else if (left.getTypeStr().equals("str")){
                    LiteralNode ls = (LiteralNode) left;
                    LiteralNode rs = (LiteralNode) right;
                    if (operatorKind.equals(SymbolKind.EQEQ)) {
                        node.setResult(String.valueOf(ls.getLiteral().equals(rs.getLiteral())));
                    } else if (operatorKind.equals(SymbolKind.DIFF)) {
                        node.setResult(String.valueOf(!ls.getLiteral().equals(rs.getLiteral())));
                    } else {
                        throw new Exception("Invalid operation: binary expression error type");
                    }
                } else{
                    NumberNode lcst = (NumberNode) left;
                    NumberNode rcst = (NumberNode) right;
                    Float lr = Float.valueOf(lcst.getValue());
                    Float rr = Float.valueOf(rcst.getValue());

                    if (operatorKind.equals(SymbolKind.EQEQ)){
                        node.setResult(String.valueOf(lr == rr));
                    }
                    else if (operatorKind.equals(SymbolKind.DIFF)){
                        node.setResult(String.valueOf(lr != rr));
                    }
                    else if (operatorKind.equals(SymbolKind.LESS)){
                        node.setResult(String.valueOf(lr < rr));
                    }
                    else if (operatorKind.equals(SymbolKind.MORE)){
                        node.setResult(String.valueOf(lr > rr));
                    } else if (operatorKind.equals(SymbolKind.LESSEQ)){
                        node.setResult(String.valueOf(lr <= rr));
                    }
                    else if (operatorKind.equals(SymbolKind.MOREEQ)){
                        node.setResult(String.valueOf(lr >= rr));
                    }
                    else {
                        throw new Exception("Invalid operation: binary expression error type");
                    }
                    break;
                }
        }
    }
}
