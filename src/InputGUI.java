package Tucil3_13523025_13523030.src;
public class InputGUI extends JFrame {
    private Papan papan;
    private BoardPanel boardPanel;
    private JComboBox<String> directionBox;
    private JSpinner rowSpinner, colSpinner, lengthSpinner;
    private JCheckBox isRedCar;
    private JButton addButton, solveButton;

    public InputGUI() {
        papan = new Papan(6, 6); // default 6x6
        boardPanel = new BoardPanel(papan);

        directionBox = new JComboBox<>(new String[]{"Horizontal", "Vertical"});
        rowSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 5, 1));
        colSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 5, 1));
        lengthSpinner = new JSpinner(new SpinnerNumberModel(2, 2, 4, 1));
        isRedCar = new JCheckBox("Primary (Red)");

        addButton = new JButton("Add Piece");
        solveButton = new JButton("Solve");

        addButton.addActionListener(e -> {
            int row = (int) rowSpinner.getValue();
            int col = (int) colSpinner.getValue();
            int len = (int) lengthSpinner.getValue();
            boolean isHorizontal = directionBox.getSelectedItem().equals("Horizontal");
            boolean isPrimary = isRedCar.isSelected();

            Piece p = new Piece((char)('A' + papan.getPieces().size()), row, col, len, isHorizontal, isPrimary);
            if (papan.placePiece(p)) {
                boardPanel.repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid placement");
            }
        });

        solveButton.addActionListener(e -> {
            List<Gerakan> steps = new UCS(papan).solve();
            new GUI(papan); // open the animated GUI window
        });

        JPanel controlPanel = new JPanel();
        controlPanel.add(new JLabel("Dir:")); controlPanel.add(directionBox);
        controlPanel.add(new JLabel("Row:")); controlPanel.add(rowSpinner);
        controlPanel.add(new JLabel("Col:")); controlPanel.add(colSpinner);
        controlPanel.add(new JLabel("Len:")); controlPanel.add(lengthSpinner);
        controlPanel.add(isRedCar);
        controlPanel.add(addButton);
        controlPanel.add(solveButton);

        setLayout(new BorderLayout());
        add(boardPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        setTitle("Rush Hour Setup");
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
