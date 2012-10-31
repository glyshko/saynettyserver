class CreateLogins < ActiveRecord::Migration
  def change
    create_table :logins do |t|
      t.string :ipadress
      t.integer :port
      t.string :channelid
      t.string :state
      t.string :acceskey

      t.timestamps
    end
  end
end
