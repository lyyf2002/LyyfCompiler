import java.util.ArrayList;

public class IfNode extends ASTNode{
    //'if' '(' Cond ')' Stmt [ 'else' Stmt ]
    /*
    &&短路
    j~cond1 else
    j~cond2 else
    j~cond3 else
    then:
        thenstmt
        j end
    else:
        elsestmt
    end:

    ||短路
    jcond1 then
    jcond2 then
    jcond3 then
    j else
    then:
        thenstmt
        j end
    else:
        elsestmt
    end:

if con1&&con2&&con3 || con4&&con5&&con6
    thenstmt
else
    elsestmt


    j ~cond1 label1
    j ~cond2 label1
    j ~cond3 label1
    j then
label1:
    j ~cond4 else
    j ~cond5 else
    j ~cond6 else
    j then

then:
    tnenstmt
    j end
else:
    elsestmt
end:

     */
    private CondNode condNode;
    private StmtNode thenStmt;
    private StmtNode elseStmt;
    public IfNode(ArrayList<Node> nodes,SymbolTable symbolTable,boolean isReturnInt,boolean isLoop) {
        super(symbolTable);
        int i;
        for (i=2;i<nodes.size();i++){
            if (nodes.get(i) instanceof NonTermianlNode){
                condNode = new CondNode(nodes.get(i),symbolTable);
                break;
            }
        }
        for (i++;i<nodes.size();i++){
            if (nodes.get(i) instanceof NonTermianlNode){
                thenStmt = new StmtNode(nodes.get(i),symbolTable,false,isReturnInt,isLoop);
                break;
            }
        }
        for (i++;i<nodes.size();i++){
            if (nodes.get(i) instanceof NonTermianlNode){
                elseStmt = new StmtNode(nodes.get(i),symbolTable,false,isReturnInt,isLoop);
                break;
            }
        }
    }

    @Override
    public void intoMid() {
        String THEN="then"+IdCtrl.getLabelId();

        String ELSE=elseStmt!=null?"else"+IdCtrl.getLabelId():null;
        String END = "end"+IdCtrl.getLabelId();
        condNode.cond2Mid(THEN,ELSE==null?END:ELSE);

        MidCode.add(new FourUnit("label",null,null,THEN));

        thenStmt.intoMid();
        if (ELSE!=null){
            MidCode.add(new FourUnit("j",null,null,END));
            MidCode.add(new FourUnit("label",null,null,ELSE));
            elseStmt.intoMid();
        }

        MidCode.add(new FourUnit("label",null,null,END));
    }
}
/*
if con1&&con2&&con3 || con4&&con5&&con6
        thenstmt
        else
        elsestmt


        j ~cond1 label1
        j ~cond2 label1
        j ~cond3 label1
        j then
        label1:
        j ~cond4 else
        j ~cond5 else
        j ~cond6 else

        then:
        tnenstmt
        j end
        else:
        elsestmt
        end:




        */