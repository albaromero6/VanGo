<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        Schema::create('vehiculo', function (Blueprint $table) {
            $table->id('idVeh'); 
            $table->string('marca', 30);
            $table->string('modelo', 30);
            $table->integer('año'); 
            $table->integer('capacidad');
            $table->decimal('precio_dia', 8, 2); 
            $table->text('descripcion', 10);
            $table->boolean('disponible')->default(true);
            $table->string('imagen')->nullable();
            $table->timestamps(); 
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('vehiculo');
    }
};
