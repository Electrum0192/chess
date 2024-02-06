package chess;

public class DoubleMove extends ChessMove{
    public DoubleMove(ChessPosition startPosition, ChessPosition endPosition, ChessPosition startTwo, ChessPosition endTwo) {
        super(startPosition,endPosition);
        this.startTwo = startTwo;
        this.endTwo = endTwo;
    }

    /**
     * @return ChessPosition of second piece's starting location
     */
    public ChessPosition getStartTwo() {
        return startTwo;
    }

    /**
     * @return ChessPosition of second piece's ending location
     */
    public ChessPosition getEndTwo() {
        return endTwo;
    }

    private final ChessPosition startTwo;
    private final ChessPosition endTwo;
}
