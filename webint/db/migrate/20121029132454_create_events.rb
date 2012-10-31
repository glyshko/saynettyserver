class CreateEvents < ActiveRecord::Migration
  def change
    create_table :events do |t|
      t.string :channelid
      t.string :command
      t.boolean :status

      t.timestamps
    end
  end
end
