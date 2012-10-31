# encoding: UTF-8
# This file is auto-generated from the current state of the database. Instead
# of editing this file, please use the migrations feature of Active Record to
# incrementally modify your database, and then regenerate this schema definition.
#
# Note that this schema.rb definition is the authoritative source for your
# database schema. If you need to create the application database on another
# system, you should be using db:schema:load, not running all the migrations
# from scratch. The latter is a flawed and unsustainable approach (the more migrations
# you'll amass, the slower it'll run and the greater likelihood for issues).
#
# It's strongly recommended to check this file into your version control system.

ActiveRecord::Schema.define(:version => 20121030085856) do

  create_table "actives", :id => false, :force => true do |t|
    t.string "name"
    t.string "channelid"
  end

  create_table "events", :force => true do |t|
    t.string   "channelid"
    t.string   "command"
    t.integer  "status"
    t.datetime "created_at", :null => false
    t.datetime "updated_at", :null => false
  end

  create_table "events2", :force => true do |t|
    t.string "hostip"
    t.string "accesskey"
    t.string "command"
    t.string "status"
  end

  create_table "hosts", :force => true do |t|
    t.string   "acceskey"
    t.boolean  "active"
    t.datetime "created_at", :null => false
    t.datetime "updated_at", :null => false
    t.string   "name"
  end

  create_table "hosts2", :force => true do |t|
    t.string  "key",    :null => false
    t.boolean "active", :null => false
  end

  create_table "logins", :force => true do |t|
    t.string   "ipadress"
    t.integer  "port"
    t.string   "channelid"
    t.string   "state"
    t.string   "acceskey"
    t.datetime "created_at", :null => false
    t.datetime "updated_at", :null => false
  end

  create_table "logins2", :force => true do |t|
    t.string    "ipadress",                :null => false
    t.integer   "port",                    :null => false
    t.timestamp "created_at",              :null => false
    t.string    "channelid",               :null => false
    t.string    "state",      :limit => 4, :null => false
    t.string    "accesskey",               :null => false
  end

  create_table "message2", :force => true do |t|
    t.string    "mesg"
    t.string    "channelid"
    t.timestamp "created_at", :null => false
  end

  add_index "message2", ["channelid"], :name => "chennelid"

  create_table "messages", :force => true do |t|
    t.string   "msg"
    t.string   "channelid"
    t.datetime "created_at", :null => false
    t.datetime "updated_at", :null => false
  end

end
