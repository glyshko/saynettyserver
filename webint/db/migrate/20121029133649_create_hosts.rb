class CreateHosts < ActiveRecord::Migration
  def change
    create_table :hosts do |t|
      t.string :acceskey
      t.boolean :active

      t.timestamps
    end
  end
end
