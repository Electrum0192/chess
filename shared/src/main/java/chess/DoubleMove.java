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
    public ChessPosition getStartPosition() {
        return startTwo;
    }

    /**
     * @return ChessPosition of second piece's ending location
     */
    public ChessPosition getEndPosition() {
        return endTwo;
    }

    private final ChessPosition startTwo;
    private final ChessPosition endTwo;
}
