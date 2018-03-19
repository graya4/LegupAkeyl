package puzzle.fillapix.rules;

import model.gameboard.Board;
import model.rules.BasicRule;
import model.tree.TreeTransition;
import puzzle.fillapix.FillapixBoard;
import puzzle.fillapix.FillapixCell;

import java.util.ArrayList;

public class FinishWithWhiteBasicRule extends BasicRule {
    public FinishWithWhiteBasicRule() {
        super("Finish with White",
                "The remaining unknowns around and on a cell must be white to satisfy the number",
                "images/fillapix/rules/FinishWithWhite.png");
    }

    @Override
    public String checkRuleAt(TreeTransition transition, int elementIndex) {
        FillapixBoard fillapixBoard = (FillapixBoard) transition.getBoard();
        int width = fillapixBoard.getWidth();
        int height = fillapixBoard.getHeight();
        FillapixCell cell = fillapixBoard.getCell(elementIndex%width,elementIndex/width);

        BlackOrWhiteCaseRule blackOrWhite = new BlackOrWhiteCaseRule();
        TooManyBlackCellsContradictionRule tooManyBlackCells = new TooManyBlackCellsContradictionRule();

        FillapixBoard currentBoard = (FillapixBoard) transition.getParentNode().getBoard();
        // See note in Finish with Black because the same thing applies here for this method
        for(int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                int x = cell.getLocation().x + i;
                int y = cell.getLocation().y + j;
                if (x > -1 && x < width && y > -1 && y < height) {
                    ArrayList<Board> cases = blackOrWhite.getCases(currentBoard, x*width+y);
                    for (Board caseBoard: cases) {
                        String contradiction = tooManyBlackCells.checkContradictionAt((FillapixBoard) caseBoard,x*width+y);
                        FillapixCell caseCell = ((FillapixBoard) caseBoard).getCell(x,y);
                        if (caseCell.hasSameState(fillapixBoard.getCell(x,y))) {
                            if (contradiction==null) { // is a contradiction
                                return "Incorrect use of Finish with Black, your answer leads to a contradiction.";
                            } else {
                                currentBoard = (FillapixBoard) caseBoard;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public boolean doDefaultApplication(TreeTransition transition) {return false; }

    @Override
    public boolean doDefaultApplicationAt(TreeTransition transition, int elementIndex) {return false; }
}