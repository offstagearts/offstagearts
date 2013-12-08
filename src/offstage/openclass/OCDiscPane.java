/*
OffstageArts: Enterprise Database for Arts Organizations
This file Copyright (c) 2005-2008 by Robert Fischer

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/*
 * OCDiscPanel.java
 *
 * Created on June 10, 2008, 9:03 AM
 */

package offstage.openclass;

import citibob.app.App;
import citibob.sql.RsTasklet2;
import citibob.sql.SqlRun;
import citibob.sql.pgsql.SqlInteger;
import citibob.swing.table.DataCols;
import citibob.swing.table.DelegateStyledTM;
import citibob.swing.table.ExtPivotTableModel;
import citibob.swing.table.RenderEditCols;
import citibob.task.SqlTask;
import citibob.text.PercentSFormat;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import offstage.FrontApp;

/**
 *
 * @author  citibob
 */
public class OCDiscPane extends javax.swing.JPanel {
	
OCDiscModels models;
	
//Integer TeacherID;		// Teacher for which this shows discounts (or null for studio discounts)

App app;
	
	/** Creates new form OCDiscPanel */
	public OCDiscPane() {
		initComponents();
	}

	public OCDiscModels getModels() { return models; }
	
	public void refresh(SqlRun str, Integer TeacherID)
	{
		models.getDm().setKey(TeacherID);
		models.getDm().doSelect(str);
	}
	
//	public void initRuntime(SqlRun str, final FrontApp xapp, final Integer TeacherID)
	public void initRuntime(final FrontApp xapp, OCDiscModels models)
	{
		this.app = xapp;
		this.models = models;
//		models = new OCDiscModels(str, xapp);
		
//		str.execUpdate(new UpdTasklet2() {
//		public void run(SqlRun str) {

			// Set the model in Swing
			DelegateStyledTM stm = new DelegateStyledTM(models.getTm());
			ExtPivotTableModel epivot = models.getTm();
			stm.setModel(epivot.project(
				new String[] {"Status", "Name"},
				new String[] {"__status__", "name"}));
			RenderEditCols re = stm.setRenderEditCols(app.swingerMap());
				epivot.setFormat(stm.getModel(), ExtPivotTableModel.PIVOT, re, new PercentSFormat());
			DataCols<Boolean> editable = stm.setEditableCols();
				editable.setColumn(0, Boolean.FALSE);
				editable.setColumn(1, Boolean.TRUE);
			discounts.setStyledTM(stm);
			
//			models.getTm().setModelU(discounts,
//				new String[] {"Status", "Name"},
//				new String[] {"__status__", "name"},
//				null,
//				new boolean[] {false,true}, true,
//				app.swingerMap());
//			models.getTm().setPivotValFormat(discounts, new PercentSFormat());
			
			
//				new FormatSFormat(NumberFormat.getPercentInstance(), "", SFormat.RIGHT));
//			discounts.setModelU(models.getTm(), app.swingerMap());
//			models.getDm().setKey(TeacherID);
//			models.getDm().doSelect(str);
//		}});
		// --------------------------------------------------------
	}
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        discounts = new citibob.swing.JTypeColTable();
        jPanel1 = new javax.swing.JPanel();
        bAdd = new javax.swing.JButton();
        bRemove = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        discounts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(discounts);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);

        bAdd.setText("Add");
        bAdd.setFocusable(false);
        bAdd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        bAdd.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        bAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAddActionPerformed(evt);
            }
        });
        jPanel1.add(bAdd);

        bRemove.setText("Remove");
        bRemove.setFocusable(false);
        bRemove.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        bRemove.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        bRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bRemoveActionPerformed(evt);
            }
        });
        jPanel1.add(bRemove);

        add(jPanel1, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

	private void bAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAddActionPerformed
		app.guiRun().run(OCDiscPane.this, new SqlTask() {
		public void run(SqlRun str) throws Exception {
			models.getDm().doUpdate(str);
			str.execSql("insert into ocdiscids (teacherid) values ("
				+ SqlInteger.sql((Integer)models.getDm().getKey()) + ")");
			models.getDm().doSelect(str);
		}});
}//GEN-LAST:event_bAddActionPerformed

	private void bRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bRemoveActionPerformed
		app.guiRun().run(OCDiscPane.this, new SqlTask() {
		public void run(SqlRun str) throws Exception {
			// Figure out which discount was clicked on
			final int row = discounts.getSelectedRow();
			if (row < 0) return;
			final int ocdiscid = (Integer)models.getTm().getValueAt(row, "ocdiscid");
			
			// See how much it's used
			String sql =
				" select count(*) from ocdiscs where ocdiscid = " + ocdiscid;
			str.execSql(sql, new RsTasklet2() {
			public void run(SqlRun str, ResultSet rs) throws SQLException {
				rs.next();
				int count = rs.getInt(1);
				
				// Warn user if it's in use
				if (count > 0) {
					String name = (String)models.getTm().getValueAt(row, "name");
					String msg =
						"The discount code '" + name + "' is being used by " + count + " students.\n" +
						"Are you sure you wish to remove it and eliminate the students' participation\n" +
						"in the discount?";
					int res = JOptionPane.showConfirmDialog(OCDiscPane.this, msg,
						"Remove Discount Code?", JOptionPane.YES_NO_OPTION);
					if (res != JOptionPane.YES_OPTION) return;
				}
				
				// Remove it --- to be flushed later
				models.getTm().deleteRow(row);
//				discDm.getSchemaBuf().deleteRow(row);
//				str.execSql("delete from ocdiscids where ocdiscid = " + ocdiscid);
				
			}	});
		}});
		// TODO add your handling code here:
	}//GEN-LAST:event_bRemoveActionPerformed
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bAdd;
    private javax.swing.JButton bRemove;
    private citibob.swing.JTypeColTable discounts;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
	
}