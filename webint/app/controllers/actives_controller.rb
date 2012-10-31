class ActivesController < ApplicationController
  # GET /actives
  # GET /actives.json
  def index
    @actives = Active.all

    respond_to do |format|
      format.html # index.html.erb
      format.json { render json: @actives }
    end
  end

  # GET /actives/1
  # GET /actives/1.json
  def show
    @active = Active.find(params[:id])

    respond_to do |format|
      format.html # show.html.erb
      format.json { render json: @active }
    end
  end

  # GET /actives/new
  # GET /actives/new.json
  def new
    @active = Active.new

    respond_to do |format|
      format.html # new.html.erb
      format.json { render json: @active }
    end
  end

  # GET /actives/1/edit
  def edit
    @active = Active.find(params[:id])
  end

  # POST /actives
  # POST /actives.json
  def create
    @active = Active.new(params[:active])

    respond_to do |format|
      if @active.save
        format.html { redirect_to @active, notice: 'Active was successfully created.' }
        format.json { render json: @active, status: :created, location: @active }
      else
        format.html { render action: "new" }
        format.json { render json: @active.errors, status: :unprocessable_entity }
      end
    end
  end

  # PUT /actives/1
  # PUT /actives/1.json
  def update
    @active = Active.find(params[:id])

    respond_to do |format|
      if @active.update_attributes(params[:active])
        format.html { redirect_to @active, notice: 'Active was successfully updated.' }
        format.json { head :no_content }
      else
        format.html { render action: "edit" }
        format.json { render json: @active.errors, status: :unprocessable_entity }
      end
    end
  end

  # DELETE /actives/1
  # DELETE /actives/1.json
  def destroy
    @active = Active.find(params[:id])
    @active.destroy

    respond_to do |format|
      format.html { redirect_to actives_url }
      format.json { head :no_content }
    end
  end
end
