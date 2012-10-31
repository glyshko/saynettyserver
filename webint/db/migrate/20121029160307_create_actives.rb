class CreateActives < ActiveRecord::Migration
  def change
    create_table :actives do |t|
      t.string :name
      t.string :channelid

      t.timestamps
    end
  end
end
